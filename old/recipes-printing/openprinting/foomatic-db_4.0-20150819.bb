SUMMARY = "The collected knowledge about printers, drivers, and driver options in XML files, used by foomatic-db-engine to generate PPD files."

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=89aead728f64ad2af95c03f5793274bc"

SRC_URI = "http://www.openprinting.org/download/foomatic/foomatic-db-${PV}.tar.gz"
SRC_URI[md5sum] = "6adaea9b7383d97c3aefb58532fd66d8"
SRC_URI[sha256sum] = "e9824ae1a7b20894a4be4083e49b66ed828ccc520d55abfdfa835c1eae99dee3"

S = "${WORKDIR}/${BPN}-20150819"

inherit autotools-brokensep pkgconfig allarch

FILES_${PN} += "${datadir}/foomatic/ \
                ${datadir}/cups \
               "

PACKAGES =+ "${PN}-de ${PN}-es ${PN}-fr ${PN}-it ${PN}-pt"

FILES_${PN}-de = "${datadir}/foomatic/db/source/PPD/*/de"
FILES_${PN}-es = "${datadir}/foomatic/db/source/PPD/*/es"
FILES_${PN}-fr = "${datadir}/foomatic/db/source/PPD/*/fr"
FILES_${PN}-it = "${datadir}/foomatic/db/source/PPD/*/it"
FILES_${PN}-pt = "${datadir}/foomatic/db/source/PPD/*/pt"
