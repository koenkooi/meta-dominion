SUMMARY = "NCurses Disk Usage"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fa8c26b8ae7f146e3b8545f7932bc26"

DEPENDS = "ncurses"

SRC_URI = "https://dev.yorhel.nl/download/ncdu-${PV}.tar.gz"
SRC_URI[md5sum] = "7365ac46c420bc511621216b1747984f"
SRC_URI[sha256sum] = "820e4e4747a2a2ec7a2e9f06d2f5a353516362c22496a10a9834f871b877499a"

inherit autotools pkgconfig

