FROM debian:stretch-20200607

ARG JDK
RUN \
	apt-get update && \
	apt-get install --no-install-recommends --assume-yes \
		${JDK}-jdk-headless \
		ant \
		ant-optional \
		imagemagick \
		librsvg2-bin \
		webp \
		ghostscript && \
	rm -rf /var/lib/apt/lists/* && \
	grep --invert-match '^.*policy.*coder.*none.*PDF.*$' \
		/etc/ImageMagick-6/policy.xml > \
		/etc/ImageMagick-6/policy-new.xml && \
	mv \
		/etc/ImageMagick-6/policy-new.xml \
		/etc/ImageMagick-6/policy.xml

RUN \
	mkdir /var/dependencies-cache && \
	chown 1001:1001 /var/dependencies-cache

LABEL purpose=jenkins
LABEL jdk=${JDK}
LABEL ant=yes
LABEL ant-optional=yes
LABEL imagemagick=yes
LABEL imagemagick.svg=yes
LABEL imagemagick.webp=yes
LABEL imagemagick.pdf=yes