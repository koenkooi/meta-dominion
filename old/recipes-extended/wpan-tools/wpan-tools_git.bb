SUMMARY = "Userspace tools for Linux IEEE 802.15.4 stack"
HOMEPAGE = "http://wpan.cakelab.org/releases/"
DESCRIPTION = "This is a set of utils to manage the Linux WPAN stack via \
netlink interface. This requires recent kernel with nl802154 interface."

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=4cfd939b1d7e6aba9fcefb7f6e2fd45d"

DEPENDS = "libnl"

PV = "0.7+git${SRCPV}"
SRCREV = "77ae6f363e0bbd667f688fbfcb1a33024c026c2d"

SRC_URI = "git://github.com/linux-wpan/wpan-tools.git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
