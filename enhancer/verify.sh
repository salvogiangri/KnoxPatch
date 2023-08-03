TMPDIR_FOR_VERIFY="$TMPDIR/.vunzip"
mkdir "$TMPDIR_FOR_VERIFY"

abort_verify() {
  ui_print "*********************************************************"
  ui_print "! $1"
  ui_print "! This zip may be corrupted, please try downloading again"
  abort    "*********************************************************"
}

# extract <zip> <file> <target dir> <junk paths>
extract() {
  zip=$1
  file=$2
  dir=$3
  junk_paths=$4
  [ -z "$junk_paths" ] && junk_paths=false
  opts="-o"
  [ $junk_paths = true ] && opts="-oj"

  file_path=""
  hash_path=""
  if [ $junk_paths = true ]; then
    file_path="$dir/$(basename "$file")"
    hash_path="$TMPDIR_FOR_VERIFY/$(basename "$file").sha256"
  else
    file_path="$dir/$file"
    hash_path="$TMPDIR_FOR_VERIFY/$file.sha256"
  fi

  unzip $opts "$zip" "$file" -d "$dir" >&2
  [ -f "$file_path" ] || abort_verify "$file not exists"

  unzip $opts "$zip" "$file.sha256" -d "$TMPDIR_FOR_VERIFY" >&2
  [ -f "$hash_path" ] || abort_verify "$file.sha256 not exists"

  (echo "$(cat "$hash_path")  $file_path" | sha256sum -c -s -) || abort_verify "Failed to verify $file"
  ui_print "- Verified $file" >&1
}

# hex_check <file> <hex>
hex_check() {
  local xxd
  if xxd --help >/dev/null 2>&1; then
    xxd=xxd
  elif /system/bin/xxd --help >/dev/null 2>&1; then
    xxd="/system/bin/xxd"
  elif /system/bin/toybox xxd --help >/dev/null 2>&1; then
    xxd="/system/bin/toybox xxd"
  else
    echo " CANT LOAD XXD bin" && return
  fi
  if ! exist "$1"; then echo "CANT FIND: $1" && return 1; fi
  if $( ( $xxd -p "$1" | tr -d \\n | tr -d " " | grep -q "$2" ) 2>/dev/null ); then true; else false; fi
}

# hex_check <file> <old_hex> <new_hex>
hex_patch() {
  local xxd
  if xxd --help >/dev/null 2>&1; then
    xxd=xxd
  elif /system/bin/xxd --help >/dev/null 2>&1; then
    xxd="/system/bin/xxd"
  elif /system/bin/toybox xxd --help >/dev/null 2>&1; then
    xxd="/system/bin/toybox xxd"
  elif [ ! -e "/data/adb/magisk/magiskboot" ]; then
    echo "CANT LOAD: xxd or magiskboot" && return 1
  fi
  if ! exist "$1"; then echo "CANT FIND: $1" && return 1; fi
  testrw "$(dirname "$1")" || return 1
  if $( ( $xxd -p "$1" | tr -d \\n | tr -d " " | sed "s/$2/$3/" | $xxd -r -p > "$1.tmp" ) 2>/dev/null ); then
    if hex_check "$1.tmp" "$3"; then
      move "$1.tmp" "$1"
      true
    elif [ -e "/data/adb/magisk/magiskboot" ]; then
      if $(/data/adb/magisk/magiskboot hexpatch "$1" "$2" "$3"); then
        if hex_check "$1" "$3"; then true; else false; fi
      else
        false
      fi
    else
      false
    fi
  elif [ -e "/data/adb/magisk/magiskboot" ]; then
    if $(/data/adb/magisk/magiskboot hexpatch "$1" "$2" "$3"); then
      if hex_check "$1" "$3"; then true; else false; fi
    else
      false
    fi
  else
    false
  fi
}

file="META-INF/com/google/android/update-binary"
file_path="$TMPDIR_FOR_VERIFY/$file"
hash_path="$file_path.sha256"
unzip -o "$ZIPFILE" "META-INF/com/google/android/*" -d "$TMPDIR_FOR_VERIFY" >&2
[ -f "$file_path" ] || abort_verify "$file not exists"
if [ -f "$hash_path" ]; then
  (echo "$(cat "$hash_path")  $file_path" | sha256sum -c -s -) || abort_verify "Failed to verify $file"
  ui_print "- Verified $file" >&1
else
  ui_print "- Download from Magisk app"
fi
