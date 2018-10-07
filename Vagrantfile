# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/xenial64"

  config.vm.network "private_network", ip: "192.168.33.10"

  config.vm.provision "shell", inline: "apt update"
  config.vm.provision "shell", inline: "apt install --assume-yes /vagrant/buku_3.9-1_ubuntu16.04.amd64.deb"
end

