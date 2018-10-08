#!/usr/bin/env bash

cd ~
python3 -m pip install --user --upgrade pip
python3 -m pip install --user virtualenv
python3 -m virtualenv env
source env/bin/activate
#if [ ! -e Buku ]; then
#  git clone https://github.com/jarun/Buku
#fi
#cd Buku
ls -al
pip3 install buku[server]
bukuserver run --host 0.0.0.0 --port 5000

