# -*- mode: ruby -*-
# vi: set ft=ruby :

$copy_bookmarks_db = <<-SCRIPT
echo "copying bookmarks.db if it exists"
if [ -f /vagrant/data/bookmarks.db ]; then
  mkdir -p ~/.local/share/buku/
  cp /vagrant/data/bookmarks.db ~/.local/share/buku/
fi
SCRIPT

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/xenial64"

  config.vm.network "private_network", ip: "192.168.33.10"

  config.vm.provision "shell", inline: "apt update"

  # Install buku
  config.vm.provision "shell", inline: "apt install --assume-yes /vagrant/buku_3.9-1_ubuntu16.04.amd64.deb"

  # Provision bookmark database, if it is present in /vagrant/data/.
  config.vm.provision "shell", privileged: false, inline: $copy_bookmarks_db

  # Install bukuserver dependencies
  config.vm.provision "shell", inline: "apt install --assume-yes python3 python3-pip python3-dev libffi-dev"

end

