#!/sbin/sh
#
# KnoxPatch Enhancer
# Copyright (C) 2023 BlackMesa123
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
SKIPUNZIP=1

enforce_install_from_app() {
  if $BOOTMODE; then
    ui_print "- Installing from Magisk / KernelSU app"
  else
    ui_print "*********************************************************"
    ui_print "! Install from recovery is NOT supported"
    ui_print "! Recovery sucks"
    ui_print "! Please install from Magisk / KernelSU app"
    abort "*********************************************************"
  fi
}

enforce_sem() {
  ONEUI_VERSION=$(getprop 'ro.build.version.oneui')

  if [ -z "$ONEUI_VERSION" ]; then
    SEP_VERSION=$(getprop 'ro.build.version.sep')

    if [ -z "$SEP_VERSION" ]; then
      abort "E: This module only supports One UI devices"
    fi

    # The OneUI version is determined by the SEP version
    # (SEP 14.0 == OneUI 5.0 | [SEP - 9.0 = OneUI] )
    ONEUI_VERSION=$((SEP_VERSION - 90000))
  fi

  if [ ! "$ONEUI_VERSION" -ge "10000" ]; then
    abort "E: This module only supports One UI 1.x and above"
  fi

  if pm list features | grep -q samsung_experience_mobile_lite; then
    LABEL="One UI Core"
  else
    LABEL="One UI"
  fi

  MAJOR=$((ONEUI_VERSION / 10000))
  MINOR=$((ONEUI_VERSION % 10000 / 100))
  PATCH=$((ONEUI_VERSION % 100))

  if [ $PATCH != "0" ]; then
    ui_print "I: $LABEL version: $MAJOR.$MINOR.$PATCH"
  else
    ui_print "I: $LABEL version: $MAJOR.$MINOR"
  fi
}

VERSION=$(grep_prop version "${TMPDIR}/module.prop")
ui_print "I: KnoxPatch Enhancer: ${VERSION}"

# Extract verify.sh
ui_print "- Extracting verify.sh"
unzip -o "$ZIPFILE" 'verify.sh' -d "$TMPDIR" >&2
if [ ! -f "$TMPDIR/verify.sh" ]; then
  ui_print "*********************************************************"
  ui_print "! Unable to extract verify.sh!"
  ui_print "! This zip may be corrupted, please try downloading again"
  abort    "*********************************************************"
fi
. "$TMPDIR/verify.sh"

extract "$ZIPFILE" 'customize.sh' "$TMPDIR"
extract "$ZIPFILE" 'verify.sh' "$TMPDIR"

enforce_install_from_app
enforce_sem

if pm list packages | grep -q knoxpatch; then
  KP_VERSION=$(dumpsys package io.mesalabs.knoxpatch | grep versionName | sed 's/versionName=//g' | sed 's/ //g')
  ui_print "I: KnoxPatch installed: v$KP_VERSION"
else
  ui_print "W: KnoxPatch not installed!"
fi

ui_print "I: Extracting module files..."
extract "$ZIPFILE" 'module.prop' "$MODPATH"
extract "$ZIPFILE" 'system.prop' "$MODPATH"
mkdir -p "$MODPATH/system/etc/permissions"
extract "$ZIPFILE" 'system/etc/permissions/knoxpatch_enhancer.xml' "$MODPATH/system/etc/permissions"

if [ "$API" == "29" ] && [ "$ARCH" == "arm64" ]; then
  if grep -q 'Device supports FBE!' /system/lib/libepm.so; then
    ui_print "I: Applying Secure Folder fix..."
    mkdir -p "$MODPATH/system/bin"
    mkdir -p "$MODPATH/system/lib"
    mkdir -p "$MODPATH/system/lib64"
    extract "$ZIPFILE" 'system/bin/vold' "$MODPATH/system/bin"
    extract "$ZIPFILE" 'system/lib/libepm.so' "$MODPATH/system/lib"
    extract "$ZIPFILE" 'system/lib64/libepm.so' "$MODPATH/system/lib64"
  fi
fi

ui_print "I: Applying WSM fix..."
mkdir -p "$MODPATH/system/lib"
touch "$MODPATH/system/lib/lib.hal.wsm.samsung.so"
if [ "$IS64BIT" == "true" ]; then
  mkdir -p "$MODPATH/system/lib64"
  touch "$MODPATH/system/lib64/lib.hal.wsm.samsung.so"
fi

set_perm_recursive "$MODPATH" 0 0 0755 0644
