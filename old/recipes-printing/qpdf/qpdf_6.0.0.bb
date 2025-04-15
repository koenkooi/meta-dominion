SUMMARY = "A Content-Preserving PDF Transformation System"

LICENSE = "Artistic-2.0"
LIC_FILES_CHKSUM = "file://Artistic-2.0;md5=7806296b9fae874361e6fb10072b7ee3"

DEPENDS = "zlib pcre"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/qpdf/qpdf/${PV}/qpdf-${PV}.tar.gz"
SRC_URI[md5sum] = "e014bd3ecf1c4d1a520bbc14d84ac20e"
SRC_URI[sha256sum] = "a9fdc7e94d38fcd3831f37b6e0fe36492bf79aa6d54f8f66062cf7f9c4155233"

inherit autotools-brokensep pkgconfig

CLEANBROKEN = "1"

EXTRA_OECONF = "--with-random=/dev/urandom"
