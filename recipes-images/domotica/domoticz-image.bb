#Angstrom base image

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit core-image systemd-boot

DISTRO_UPDATE_ALTERNATIVES ??= ""
ROOTFS_PKGMANAGE_PKGS ?= '${@oe.utils.conditional("ONLINE_PACKAGE_MANAGEMENT", "none", "", "${ROOTFS_PKGMANAGE} ${DISTRO_UPDATE_ALTERNATIVES}", d)}'

# Debug features, disable if wanted
IMAGE_FEATURES += "empty-root-password allow-empty-password"

# Debug tools, leave in
IMAGE_FEATURES += "package-management nfs-client ssh-server-openssh"

CORE_IMAGE_EXTRA_INSTALL += " \
	domoticz \
	${ROOTFS_PKGMANAGE_PKGS} ${DISTROFEEDCONFIGS} \
	systemd-zram-generator systemd-boot \
	bash tar wget curl screen rsync procps pigz \
	openssh-ssh openssh-scp openssh-sftp \
	net-snmp \
	e2fsprogs-resize2fs gptfdisk parted findutils coreutils util-linux btrfs-tools \
	systemd-networkd iwd \
	systemd-analyze udev-hwdb \
	avahi-daemon avahi-utils lldpd iproute2-tc net-tools \
	htop \
	tzdata \
        cronie \
	vim \
	git \
	jq \
	pv \
	file \
	linux-firmware \
	kernel-modules \
	python3-pip \
        python3-modules \
	python3-psutil \
	python3-pycryptodomex \
	python3-setuptools \
	python3-term \
        go \
        bc \
        ethtool \
	libgpiod-tools \
	gpsd pps-tools gps-utils chrony setserial \
	dracut \
	grow-rootfs \
"

export IMAGE_BASENAME = "Domoticz-image"

IMAGE_PREPROCESS_COMMAND += "do_systemd_network ; "

do_systemd_network () {
	install -d ${IMAGE_ROOTFS}${sysconfdir}/systemd/network
	cat << EOF > ${IMAGE_ROOTFS}${sysconfdir}/systemd/network/10-en.network
[Match]
Name=en*

[Network]
DHCP=yes
LLDP=yes
EmitLLDP=yes

[DHCPv4]
RouteMetric=10

[IPv6AcceptRA]
RouteMetric=10

[Route]
Metric=10
EOF

	cat << EOF > ${IMAGE_ROOTFS}${sysconfdir}/systemd/network/11-eth.network
[Match]
Name=eth*

[Network]
DHCP=yes
LLDP=yes
EmitLLDP=yes

[DHCPv4]
RouteMetric=10

[IPv6AcceptRA]
RouteMetric=10

[Route]
Metric=10
EOF

	cat << EOF > ${IMAGE_ROOTFS}${sysconfdir}/systemd/network/12-wlan.network
[Match]
Name=wlan*

[Network]
DHCP=yes
LLDP=yes
EmitLLDP=yes

[DHCPv4]
RouteMetric=100

[IPv6AcceptRA]
RouteMetric=100

[Route]
Metric=100
EOF
}

