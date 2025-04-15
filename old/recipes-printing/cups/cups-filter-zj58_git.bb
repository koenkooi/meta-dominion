SUMMARY = "CUPS filter for thermal printer Zjiang ZJ-58"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e32b5aaad49ae3179db310c827662eb4"

DEPENDS = "cups"

PV = "0.0+git${SRCPV}"
SRCREV = "f669ba772b9dc5839a3cf9990691cf912ea443f4"
SRC_URI = "git://github.com/klirichek/zj-58.git;protocol=https"

S = "${WORKDIR}/git"

CLEANBROKEN = "1"

do_compile() {
	${CC} -o rastertozj rastertozj.c ${LDFLAGS} ${CFLAGS} -lcupsimage -lcups -Wall -fPIC
}

do_install() {
	install -d ${D}${libdir}/cups/filter/
	install -m 0755 rastertozj ${D}${libdir}/cups/filter/

	install -d ${D}${datadir}/cups/model/zjiang/
	install -m 0644 ZJ-58.ppd ${D}${datadir}/cups/model/zjiang/
}

FILES_${PN} += "${datadir} ${libdir}"
FILES_${PN}-dbg += "${libdir}/cups/filter/.debug"

RDEPENDS_${PN} = "cups"
