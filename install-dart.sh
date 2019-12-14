# Update package repo listings
sudo apt-get update

# Enable access to repos using the HTTPS protocol
sudo apt-get install apt-transport-https

# Add Google linux sign in key to allow Dart repo registry
sudo sh -c 'curl https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -'

# Register Dart repo (release version)
sudo sh -c 'curl https://storage.googleapis.com/download.dartlang.org/linux/debian/dart_stable.list > /etc/apt/sources.list.d/dart_stable.list'

# or Register Dart repo (dev version)
# sudo sh -c 'curl https://storage.googleapis.com/download.dartlang.org/linux/debian/dart_unstable.list > /etc/apt/sources.list.d/dart_unstable.list'

# Install Dart SDK
sudo apt-get update
sudo apt-get install dart
