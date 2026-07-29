SUMMARY = "Domoticz is a Home Automation system design to control various devices and receive input from various sensors. "

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "python3 lua sqlite3 boost curl openssl libusb zlib openzwave mosquitto jsoncpp minizip"

inherit cmake pkgconfig useradd systemd manpages python3targetconfig

# Domoticz's own build number (getgit.cmake: git rev-list HEAD --count + a
# fixed 2107 offset), deduced without needing a build: git rev-list --count
# of SRCREV plus this recipe's 50 applied patch commits, from the
# koen-features-WIP branch in ~/Projects/Domoticz/domoticz (that branch
# mirrors SRCREV + every patch in SRC_URI as real commits). Recompute after
# bumping SRCREV or changing the patch count:
#   git -C ~/Projects/Domoticz/domoticz rev-list koen-features-WIP --count
#   # add 2107 to the result
DOMOTICZ_BUILDNUMBER = "18207"
PV = "2026.3.${DOMOTICZ_BUILDNUMBER}+git"

SRCREV = "f734fde31a4c2ecab3edd825ceb9854719444f64"
SRCREV_jwtcpp = "3e037df3e669633a3044618e30550ea2f212e915"
SRCREV_libwebem = "9126af0ce3456c24b936dfb20d1b5a8bdb797104"
SRCREV_FORMAT = "default_jwtcpp_libwebem"
S = "${UNPACKDIR}/domoticz"

PATCHTOOL = "git"

SRC_URI = "git://github.com/domoticz/domoticz.git;protocol=https;branch=master;destsuffix=domoticz \
           git://github.com/Thalhammer/jwt-cpp.git;protocol=https;name=jwtcpp;branch=master;destsuffix=domoticz/extern/jwtcpp \
           git://github.com/domoticz/libwebem.git;protocol=https;name=libwebem;branch=master;destsuffix=domoticz/extern/libwebem \
           file://0001-DucoBoxSerial-add-native-DucoBox-ventilation-plugin.patch;patch=1 \
           file://0002-Plugwise-appliance-ID-extraction-and-stable-Domoticz.patch;patch=1 \
           file://0003-Plugwise-include-appliance-name-in-sensor-name.patch;patch=1 \
           file://0004-Pressure-allow-2-decimals-for-pressure-sensors.patch;patch=1 \
           file://0005-Plugwise-allow-multiple-sensors-of-the-same-kind.patch;patch=1 \
           file://0006-AnnaThermostat-add-Hub-API-v3-endpoint-fallbacks.patch;patch=1 \
           file://0007-AnnaThermostat-add-actuator-write-routing-with-API-f.patch;patch=1 \
           file://0008-AnnaThermostat-add-zone-thermostat-controls-and-read.patch;patch=1 \
           file://0009-AnnaThermostat-publish-gateway-model-and-firmware.patch;patch=1 \
           file://0010-AnnaThermostat-expose-enabled-gateway-features.patch;patch=1 \
           file://0011-AnnaThermostat-add-generic-control-functionality-map.patch;patch=1 \
           file://0012-AnnaThermostat-add-support-for-humidity-sensors.patch;patch=1 \
           file://0013-AnnaThermostat-add-support-for-valve_position.patch;patch=1 \
           file://0014-Plugwise-retrieve-and-propagate-battery-status.patch;patch=1 \
           file://0015-AnnaThermostat-add-support-for-maximum_modulation_le.patch;patch=1 \
           file://0016-AnnaThermostat-add-support-for-lan_state.patch;patch=1 \
           file://0017-AnnaThermostat-add-support-for-wlan_state.patch;patch=1 \
           file://0018-AnnaThermostat-add-support-for-intended_central_heat.patch;patch=1 \
           file://0019-AnnaThermostat-add-support-for-domestic_hot_water_st.patch;patch=1 \
           file://0020-AnnaThermostat-add-support-for-central_heating_state.patch;patch=1 \
           file://0021-AnnaThermostat-add-support-for-relay-state.patch;patch=1 \
           file://0022-AnnaThermostat-add-support-for-electricity_consumed.patch;patch=1 \
           file://0023-AnnaThermostat-add-generic-measurement-fallback.patch;patch=1 \
           file://0024-AnnaThermostat-prefer-direct-corrected-temperature.patch;patch=1 \
           file://0025-AnnaThermostat-add-cooling_enabled-measurement.patch;patch=1 \
           file://0026-AnnaThermostat-add-cooling_state-measurement.patch;patch=1 \
           file://0027-AnnaThermostat-add-domestic_hot_water_comfort_mode-m.patch;patch=1 \
           file://0028-AnnaThermostat-add-domestic_hot_water_setpoint-measu.patch;patch=1 \
           file://0029-AnnaThermostat-add-electricity_produced-measurement.patch;patch=1 \
           file://0030-AnnaThermostat-add-failed_burner_flame_ignitions-mea.patch;patch=1 \
           file://0031-AnnaThermostat-add-gateway_mode-measurement.patch;patch=1 \
           file://0032-AnnaThermostat-add-intended_domestic_hot_water_comfo.patch;patch=1 \
           file://0033-AnnaThermostat-add-lan_ip_address-measurement.patch;patch=1 \
           file://0034-AnnaThermostat-add-open_therm_power_mode-measurement.patch;patch=1 \
           file://0035-AnnaThermostat-add-outdoor_temperature-measurement.patch;patch=1 \
           file://0036-AnnaThermostat-add-override_mode-measurement.patch;patch=1 \
           file://0037-AnnaThermostat-add-regulation_mode-measurement.patch;patch=1 \
           file://0038-AnnaThermostat-add-solar_irradiance-measurement.patch;patch=1 \
           file://0039-AnnaThermostat-add-weather_description-measurement.patch;patch=1 \
           file://0040-AnnaThermostat-add-wind_vector-measurement.patch;patch=1 \
           file://0041-AnnaThermostat-dedupe-point-logs-and-prefer-TempHum.patch;patch=1 \
           file://0042-AnnaThermostat-remove-stale-maintainer-note.patch;patch=1 \
           file://0043-Daikin-add-fault-code-compressor-telemetry-and-speci.patch;patch=1 \
           file://0044-Daikin-track-lifetime-energy-usage-in-process-port-o.patch;patch=1 \
           file://0045-Watchdog-don-t-read-a-stepped-clock-as-a-hung-thread.patch;patch=1 \
           file://0046-Mfi-add-native-hardware-plugin-for-Ubiquiti-mFi-mPow.patch;patch=1 \
           file://0047-Fix-Philips-Hue-bridge-registration-with-hidden-port.patch;patch=1 \
           file://0048-remoteDomoticz-accept-pre-SIGNv2-AUTHv2-and-bare-AUT.patch;patch=1 \
           file://0049-PhoenixInverterSerial-add-Phoenixtec-RS-232-solar-in.patch;patch=1 \
           file://0050-OnkyoAVTCP-fix-core-bugs-and-add-network-standby-mut.patch;patch=1 \
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

# Force ARM mode on armv4/armv5 (qemuarm-armv5te): Thumb-1's ±4MB BL range
# overflows linking boost::asio's TLS emulation call graph. Same fix oe-core
# uses for boost/gmp/icu on this tune.
ARM_INSTRUCTION_SET:armv4 = "arm"
ARM_INSTRUCTION_SET:armv5 = "arm"

# armv5 has no native 64-bit atomic instructions, so gcc emits libatomic
# calls (__atomic_store_8 etc) for domoticz's std::atomic<uint64_t> use.
# Not linked by default; cmake's own atomic check doesn't catch this case.
# --no-as-needed bracket is required: CMAKE_EXE_LINKER_FLAGS places -latomic
# before the object files, so plain -Wl,--as-needed (in TARGET_LDFLAGS)
# drops it as "not yet needed" before the linker even reaches the objects
# that reference __atomic_store_8.
LDFLAGS:append:armv5 = " -Wl,--no-as-needed -latomic -Wl,--as-needed"
