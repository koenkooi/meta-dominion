FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Blanket -- applies to every meta-sunxi machine building linux-mainline, not
# just one board. See files/sunxi-features.cfg for what it adds and why
# (cgroups/USER_NS for podman, USB WiFi dongle drivers, dm-crypt/userspace
# crypto, zram/squashfs). Same pattern as meta-beagleboard's
# linux-stable_%.bbappend + makeitwork.cfg.
SRC_URI:append = " file://sunxi-features.cfg"
