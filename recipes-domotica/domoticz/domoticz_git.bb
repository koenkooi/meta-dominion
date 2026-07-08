SUMMARY = "Domoticz is a Home Automation system design to control various devices and receive input from various sensors. "

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "python3 lua sqlite3 boost curl openssl libusb zlib openzwave mosquitto jsoncpp minizip"

inherit cmake pkgconfig useradd systemd manpages python3targetconfig

PV = "2026.2+git${SRCPV}"

SRCREV = "facfb476a1729ab4d0fb992b47bd28c0418efcf7"
SRCREV_jwtcpp = "3e037df3e669633a3044618e30550ea2f212e915"
SRCREV_libwebem = "0aa092796657c7007f04eca66471c164bbec7ada"
SRCREV_FORMAT = "default_jwtcpp_libwebem"
S = "${UNPACKDIR}/domoticz"

SRC_URI = "git://github.com/domoticz/domoticz.git;protocol=https;branch=master;destsuffix=domoticz \
           git://github.com/Thalhammer/jwt-cpp.git;protocol=https;name=jwtcpp;branch=master;destsuffix=domoticz/extern/jwtcpp \
           git://github.com/domoticz/libwebem.git;protocol=https;name=libwebem;branch=master;destsuffix=domoticz/extern/libwebem \
           file://0001-Plugwise-extract-ID-for-all-appliances-not-just-Adam.patch;patch=1 \
           file://0002-Pressure-allow-2-decimals-for-pressure-sensors.patch;patch=1 \
           file://0003-Plugwise-use-last-2-bytes-of-Appliance-ID-as-domotic.patch;patch=1 \
           file://0004-Plugwise-include-appliance-name-in-sensor-name.patch;patch=1 \
           file://0005-AnnaThermostat-add-support-for-humidity-sensors.patch;patch=1 \
           file://0006-AnnaThermostat-add-support-for-valve_position.patch \
           file://domoticz.service \
          "


EXTRA_OECMAKE = " -DWITH_LIBUSB=YES \
                  -DBOOST_INCLUDEDIR=${STAGING_INCDIR} \
                  -DUSE_STATIC_BOOST=NO \
                  -DOPENSSL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENSSL_LIBRARIES=${STAGING_LIBDIR} \
                  -DUSE_OPENSSL_STATIC=NO \
                  -DCURL_LIBRARIES=${STAGING_LIBDIR} \
                  -DCURL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOpenZWave=${STAGING_LIBDIR}/libopenzwave.so \
                  -DOPENZWAVE_LIBRARY_DIRS=${STAGING_LIBDIR} \
                  -DOPENZWAVE_INCLUDE_DIRS=${STAGING_INCDIR}/openzwave \
                  -DUSE_STATIC_OPENZWAVE=NO \
                  -DUSE_STATIC_LIBSTDCXX=NO \
                  -DUSE_BUILTIN_MINIZIP=NO \
                  -DUSE_BUILTIN_SQLITE=NO \
                  -DUSE_BUILTIN_MQTT=NO \
                  -DUSE_BUILTIN_JSONCPP=NO \
                  -DUSE_PRECOMPILED_HEADER=NO \
                  -DGIT_SUBMODULE=NO \
                  -DDISABLE_UPDATER=YES \
"

CXXFLAGS:append = " -std=c++17 -flto=jobserver"

do_install:append() {
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
            ${UNPACKDIR}/domoticz.service > ${D}${systemd_unitdir}/system/domoticz.service
}

FILES:${PN}-dbg += "${localstatedir}/lib/domoticz/.debug/"

SYSTEMD_SERVICE:${PN} = "domoticz.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = " \
    --system --no-create-home \
    --home ${localstatedir}/lib/domoticz \
    --groups dialout,plugdev \
    --user-group domoticz \
"

# Domoticz is mostly used in combination with a smart meter (ftdi dongles) or an rftrxx (acm based).
RRECOMMENDS:${PN} += "python3 \
                      rtl433 \
                      kernel-module-cdc-acm \
                      kernel-module-usbserial \
                     "

RDEPENDS:${PN} += "bash"
