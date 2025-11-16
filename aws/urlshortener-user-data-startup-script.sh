#!/bin/bash

# REMEMBER! This script is run by the `root` user! To make any file changes in `/home/ec2-user` directory you must 
# run the command with `sudo -u ec2-user` prefix.

# install docker
sudo dnf update -y
sudo dnf install docker -y
# Enable Docker to start on boot
sudo systemctl enable docker
# Start Docker service
sudo systemctl start docker
# Add ec2-user to docker group (to run docker without sudo)
sudo usermod -aG docker ec2-user

# install git
sudo dnf install git -y
# install java
sudo dnf install java-21-amazon-corretto-devel -y
# install maven 
sudo dnf install maven -y
# clone repo
sudo -u ec2-user git clone https://github.com/HorabikJ/urlshortener.git /home/ec2-user/code/urlshortener

# install zsh
sudo dnf install zsh -y
# set zsh as default shell for ec2-user user
sudo sed -i "/^ec2-user:/s|:/bin/bash$|:/usr/bin/zsh|" /etc/passwd

# install oh-my-zsh
sudo -u ec2-user sh -c 'export RUNZSH=no; export CHSH=no; sh -c "$(curl -fsSL https://raw.githubusercontent.com/robbyrussell/oh-my-zsh/master/tools/install.sh)" "" --unattended'
sudo -u ec2-user git clone https://github.com/romkatv/powerlevel10k.git /home/ec2-user/.oh-my-zsh/custom/themes/powerlevel10k
sudo -u ec2-user git clone https://github.com/zsh-users/zsh-autosuggestions /home/ec2-user/.oh-my-zsh/custom/plugins/zsh-autosuggestions
sudo -u ec2-user git clone https://github.com/zsh-users/zsh-syntax-highlighting.git /home/ec2-user/.oh-my-zsh/custom/plugins/zsh-syntax-highlighting
sudo -u ec2-user sed -i 's/^ZSH_THEME=.*$/ZSH_THEME="powerlevel10k\/powerlevel10k"/' /home/ec2-user/.zshrc
sudo -u ec2-user sed -i 's/^plugins=(.*)$/plugins=(git zsh-autosuggestions zsh-syntax-highlighting)/' /home/ec2-user/.zshrc

# apply all the changes made in /home/ec2-user/.zshrc 
source /home/ec2-user/.zshrc

# do need below 2 lines if we are using Docker to ru the app
# cd ~/code/urlshortener/ || exit
# mvn dependency:resolve
