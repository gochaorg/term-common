#!/bin/bash

opts="-con unix -nix.async"
opts="-con unix"
java -cp "target/runner/jars/*" xyz.cofe.term.common.demo.Main $opts

