# check=error=true;skip=InvalidDefaultArgInFrom
ARG DEBIAN_RELEASE
ARG DEBIAN_SNAPSHOT
FROM debian:${DEBIAN_RELEASE}-${DEBIAN_SNAPSHOT}

ARG JDK
RUN <<EOF
set -e
apt-get update
apt-get install --no-install-recommends --assume-yes \
	${JDK}-jdk-headless \
	ant \
	ant-optional \
	imagemagick \
	librsvg2-bin \
	webp \
	ghostscript
rm -rf /var/lib/apt/lists/*
grep --invert-match '^.*policy.*coder.*none.*PDF.*$' \
	/etc/ImageMagick-6/policy.xml > \
	/etc/ImageMagick-6/policy-new.xml
mv \
	/etc/ImageMagick-6/policy-new.xml \
	/etc/ImageMagick-6/policy.xml
EOF

ARG JENKINS_OWNER
RUN <<EOF
set -e
mkdir /var/jenkins-build-survivor
chown ${JENKINS_OWNER} /var/jenkins-build-survivor
EOF
