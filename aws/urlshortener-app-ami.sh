#!/bin/bash
sudo yum install git -y
curl -s "https://get.sdkman.io" | bash
source "/home/ec2-user/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.5-amzn
sdk install maven 3.9.9
git clone https://github.com/HorabikJ/urlshortener.git ~/code/urlshortener
cd ~/code/urlshortener/ || exit
mvn dependency:resolve
mvn spring-boot:run -Dspring-boot.run.profiles=aws
