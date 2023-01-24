FROM debian:buster-20220418

# corresponds to .github/workflows/ant.yml
ARG JDK
RUN \
	apt-get update && \
	apt-get install --no-install-recommends --assume-yes \
		${JDK}-jdk-headless \
		ant \
		ant-optional \
		imagemagick \
		librsvg2-bin \
		ghostscript && \
	rm -rf /var/lib/apt/lists/* && \
	grep --invert-match '^.*policy.*coder.*none.*PDF.*$' \
		/etc/ImageMagick-6/policy.xml > \
		/etc/ImageMagick-6/policy-new.xml && \
	mv \
		/etc/ImageMagick-6/policy-new.xml \
		/etc/ImageMagick-6/policy.xml

LABEL purpose=jenkins
LABEL jdk=${JDK}
LABEL ant=yes
LABEL ant-optional=yes
LABEL imagemagick=yes
LABEL imagemagick.svg=yes
LABEL imagemagick.pdf=yes
