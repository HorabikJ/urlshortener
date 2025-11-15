# Update package index
sudo dnf update -y

# Install Docker
sudo dnf install docker -y

# Start Docker service
sudo systemctl start docker

# Enable Docker to start on boot
sudo systemctl enable docker

# Add ec2-user to docker group (to run docker without sudo)
sudo usermod -aG docker ec2-user

# Log out and back in for group changes to take effect
# Or run: 
newgrp docker
