SUMMARY = "A C++ library to control Z-Wave Networks via a USB Z-Wave Controller."
HOMEPAGE = "http://www.openzwave.com/"

LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://licenses/lgpl.txt;md5=7be289db0a5cd2c8acf72a8cbd0c15df"

inherit pkgconfig

DEPENDS = "udev coreutils-native libxml2-native"

SRCREV = "a9d57fccf045d6adca8ab172f982688844ddb512"
# counts git commits since 1.6 tag
# ... and upstream changed the counter algo to go backwards a thousand commits :(
PE = "1"
PV = "1.6.1570"

SRC_URI = "git://github.com/OpenZWave/open-zwave.git;protocol=https \
          "

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "PREFIX=${prefix} \
                BITBAKE_ENV=1 \
                MACHINE=${TARGET_ARCH} \
                DESTDIR=${D} \
                SYSCONFDIR=${sysconfdir}/openzwave \
                sysconfdir=${sysconfdir}/openzwave \
                instlibdir=${libdir} \
                pkgconfigdir=${libdir}/pkgconfig "

do_compile() {
	sed -i -e 's:$(PREFIX)/etc:${sysconfdir}:g' ${S}/cpp/build/Makefile
	sed -i -e 's,pkgconfigdir ?,pkgconfigdir :,g' ${S}/cpp/build/support.mk
	oe_runmake
}

do_install() {
	oe_runmake install
}

PACKAGES =+ "libopenzwave"
FILES_libopenzwave = "${libdir}/libopenzwave${SOLIBS}"

FILES_${PN}-dev += "${bindir}/ozw_config"
