SUMMARY = "Platform support library used by libCEC and binary add-ons for Kodi"
HOMEPAGE = "http://libcec.pulse-eight.com/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://src/util/XMLUtils.cpp;beginline=2;endline=18;md5=dae8e846500e70dd8ecee55f3f018c30"

DEPENDS = "libtinyxml kodi"

PV = "17.0.0"

SRCREV = "c8188d82678fec6b784597db69a68e74ff4986b5"
SRC_URI = "git://github.com/xbmc/kodi-platform.git \
           file://kodi-platform-01_crosscompile-badness.patch \
           file://kodi-platform-02_no-multi-lib.patch \
          "

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE = " -DCMAKE_INSTALL_PREFIX_TOOLCHAIN=${STAGING_DIR_HOST}${prefix} \
                  -DCMAKE_INSTALL_LIBDIR=${libdir} \
                  -DCMAKE_INSTALL_LIBDIR_NOARCH=${libdir} \
                  -DCMAKE_MODULE_PATH=${STAGING_DIR_HOST}${libdir}/kodi \
                  -DCMAKE_PREFIX_PATH=${STAGING_DIR_HOST}${prefix} \
                "

do_compile_prepend() {
	sed -i -e 's:I/usr/include:I${STAGING_INCDIR}:g' \
	       -e 's:-pipe:${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -pipe:g' \
	          ${B}/CMakeFiles/kodiplatform.dir/flags.make
	sed -i -e 's:-pipe:${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -pipe:g'\
	          ${B}/CMakeFiles/kodiplatform.dir/link.txt
}

RPROVIDES_${PN} += "libkodiplatform"
PACKAGES =+ "libkodiplatform"

FILES_libkodiplatform = "${libdir}/lib*.so.*"

FILES_${PN}-dev += "${libdir}/*platform"

