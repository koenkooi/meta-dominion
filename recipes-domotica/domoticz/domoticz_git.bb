SUMMARY = "Domoticz is a Home Automation system design to control various devices and receive input from various sensors. "

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "lua sqlite3 boost curl openssl libusb zlib openzwave mosquitto"

inherit cmake pkgconfig useradd systemd

PV = "4.10577+git${SRCPV}"

SRCREV = "9c9319ace1f0a76b12bf349d62f455a506d286df"
SRC_URI = "git://github.com/domoticz/domoticz.git;protocol=https;branch=development \
           file://0001-WebServer-crude-workaround-for-buffer-overflow.patch \
           file://domoticz.service \
          "

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " -DWITH_LIBUSB=YES \
                  -DWITH_GPIO=YES \
                  -DBOOST_INCLUDEDIR=${STAGING_INCDIR} \
                  -DUSE_STATIC_BOOST=NO \
                  -DOPENSSL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENSSL_LIBRARIES=${STAGING_LIBDIR} \
                  -DUSE_OPENSSL_STATIC=NO \
                  -DCURL_LIBRARIES=${STAGING_LIBDIR} \
                  -DCURL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENZWAVE_LIBRARY_DIRS=${STAGING_LIBDIR} \
                  -DUSE_STATIC_OPENZWAVE=NO \
                  -DUSE_STATIC_LIBSTDCXX=NO \
                  -DUSE_BUILTIN_MQTT=NO \
                  -DUSE_BUILTIN_SQLITE=NO \
                  -DUSE_BUILTIN_ZLIB=NO \
                "

CXXFLAGS_append = " -std=c++11 -flto=jobserver"

do_install_append() {
    # The domoticz manual says "run from git checkout", but we don't tolerate such nonsense
    # and since 'make install' doesn't work properly, we do some massaging.
    install -d ${D}/foo
    mv ${D}${prefix}/* ${D}/foo

    install -d ${D}${localstatedir}/lib/domoticz
    mv ${D}/foo/* ${D}${localstatedir}/lib/domoticz

    rmdir ${D}/foo

    # Webserver files, 'wwwroot'
    install -d ${D}${datadir}/${BPN}
    mv ${D}${localstatedir}/lib/domoticz/www ${D}${datadir}/${BPN}

    # Data files and scripts, 'approot'
    # keep them in /var/lib/domoticz

    # Executables
    install -d ${D}${bindir} 
    mv ${D}${localstatedir}/lib/domoticz/domoticz ${D}${bindir}
    # internal update script, disable
    rm -f ${D}${localstatedir}/lib/domoticz/updatedomo

    chown -R domoticz ${D}${localstatedir}/lib

    install -d ${D}${systemd_unitdir}/system
    sed -e s:LIBDIR:${localstatedir}/lib:g \
        -e s:BINDIR:${bindir}:g \
	-e s:/var/lib/domoticz/www:${datadir}/${BPN}/www:g \
        -e s:/var:${localstatedir}:g \
            ${WORKDIR}/domoticz.service > ${D}${systemd_unitdir}/system/domoticz.service
}

FILES_${PN}-dbg += "${localstatedir}/lib/domoticz/.debug/"

SYSTEMD_SERVICE_${PN} = "domoticz.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = " \
    --system --no-create-home \
    --home ${localstatedir}/lib/domoticz \
    --groups dialout \
    --user-group domoticz"

# Domoticz is mostly used in combination with a smart meter (ftdi dongles) or an rftrxx (acm based).
RRECOMMENDS_${PN} += "python3 \
                      kernel-module-cdc-acm \
                      kernel-module-usbserial \
                     "
