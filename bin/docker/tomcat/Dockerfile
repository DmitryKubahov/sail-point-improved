FROM tomcat:${TOMCAT_VERSION}

#Add waiting script
COPY ./sp-mysql-ping.sh /home/sp-mysql-ping.sh

#Add waiting script
RUN chmod +x /home/sp-mysql-ping.sh

#Update all packages
RUN apt update

#Install mysql-client to check DB
RUN apt install mysql-client -y