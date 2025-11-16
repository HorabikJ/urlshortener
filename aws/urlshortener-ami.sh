#!/bin/bash

# install docker
sudo dnf update -y
sudo dnf install docker -y
# Enable Docker to start on boot
sudo systemctl enable docker
# Start Docker service
sudo systemctl start docker
# Add ec2-user to docker group (to run docker without sudo)
sudo usermod -aG docker ec2-user
newgrp docker

# install zsh
sudo dnf install zsh -y
#set zsh as default shell
sudo usermod -s /usr/bin/zsh "$(whoami)"

# install oh-my-zsh
sh -c "$(curl -fsSL https://raw.githubusercontent.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
git clone https://github.com/romkatv/powerlevel10k.git $ZSH_CUSTOM/themes/powerlevel10k
git clone https://github.com/zsh-users/zsh-autosuggestions ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-autosuggestions
git clone https://github.com/zsh-users/zsh-syntax-highlighting.git ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-syntax-highlighting
sed -i 's/^ZSH_THEME=.*$/ZSH_THEME="powerlevel10k\/powerlevel10k"/' ~/.zshrc
sed -i 's/^plugins=(.*)$/plugins=(git zsh-autosuggestions zsh-syntax-highlighting)/' ~/.zshrc

# install git
sudo yum install git -y
#install sdkman
curl -s "https://get.sdkman.io" | bash
source "/home/ec2-user/.sdkman/bin/sdkman-init.sh"
# install java
sdk install java 21.0.5-amzn
# install maven 
sdk install maven 3.9.9
# clone repo
git clone https://github.com/HorabikJ/urlshortener.git ~/code/urlshortener

# cd ~/code/urlshortener/ || exit
# mvn dependency:resolve
