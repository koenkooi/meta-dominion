SUMMARY = "IJS drivers"

LICENSE = "expat"
LIC_FILES_CHKSUM = "file://ijs.c;beginline=2;endline=22;md5=b9a372f0044b6534df864fff910c840d"

PV = "0.35+git${SRCPV}"
SRCREV = "0c176a91d53c85cdacd7917c76d6f659125ac3f6"

SRC_URI = "git://git.ghostscript.com/ghostpdl.git;protocol=http"

S = "${WORKDIR}/git/ijs"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-shared"

PACKAGES =+ "libijs"

# No proper library symlinks
FILES_libijs += "${libdir}/libijs-0.35.so"

INSANE_SKIP = "dev-so"
