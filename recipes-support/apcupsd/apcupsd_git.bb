SUMMARY = "Apcupsd a daemon for controlling APC UPSes"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/therealbstern/apcupsd.git;protocol=https;branch=Branch-3_14"
SRCREV = "717ddb16c886affc4da35a8cb5cd16dfedad096a"

PV = "3.14.14"

S = "${WORKDIR}/git"

inherit autotools-brokensep manpages

LD = "${CXX}"

EXTRA_OECONF = "--without-x \
                --enable-usb \
                --with-distname=${DISTRO}"

do_configure() {
    export topdir=${S}
    cp -R --no-dereference --preserve=mode,links -v ${S}/autoconf/configure.in ${S}

    if ! [ -d ${S}/platforms/${DISTRO} ] ; then
        cp -R --no-dereference --preserve=mode,links -v ${S}/platforms/unknown ${S}/platforms/${DISTRO}
    fi

    gnu-configize --force
    # install --help says '-c' is an ignored option, but it turns out that the argument to -c isn't ignored, so drop the complete '-c path/to/strip' line
    sed -i -e 's:$(INSTALL_PROGRAM) $(STRIP):$(INSTALL_PROGRAM):g' ${S}/autoconf/targets.mak
    # Searching in host dirs triggers the QA checks
    sed -i -e 's:-I/usr/local/include::g' -e 's:-L/usr/local/lib64::g' -e 's:-L/usr/local/lib::g' ${S}/configure

    # m4 macros are missing, using autotools_do_configure leads to linking errors with gethostname_re
    oe_runconf
}

do_install_append() {
    rm ${D}${datadir}/hal -rf
}

RDEPENDS_apcupsd += "bash"
