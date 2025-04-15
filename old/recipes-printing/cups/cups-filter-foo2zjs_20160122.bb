SUMMARY = "A linux printer driver for QPDL/ZJS/etc protocol"

DEPENDS = "bc-native cups ghostscript lcms"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=199;endline=215;md5=ff046c71543b23b455e739b1bba072d1"

SRCREV = "bc32225a3fa18f151e9e2bc1dd01c6d71c270384"
SRC_URI = "git://github.com/koenkooi/foo2zjs.git;protocol=https \
          "

S = "${WORKDIR}/git"

PARALLEL_MAKE = ""

do_install() {
	install -d ${D}${libdir}/cups/filter
	oe_runmake install DESTDIR=${D}
}

FILES_${PN} += "${datadir} \
                ${libdir}/cups/filter \
               "
RDEPENDS_${PN} += "ghostscript foomatic-db bash"
