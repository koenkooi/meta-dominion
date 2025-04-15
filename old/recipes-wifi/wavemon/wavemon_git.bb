SUMMARY = "Wavemon is an ncurses-based monitoring application for wireless network devices."

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "ncurses libnl"

inherit autotools-brokensep gettext

PV = "0.8.0"

SRCREV = "a33b95649a782e5563186c86a87ccbb60fa648e9"
SRC_URI = "git://github.com/uoaerg/wavemon.git;protocol=https"

S = "${WORKDIR}/git"

CFLAGS += " -pthread -I${STAGING_INCDIR}/libnl3"

do_configure_prepend() {
	cp ${S}/aclocal.m4 ${S}/acinclude.m4
}

