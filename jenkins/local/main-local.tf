provider "google" {
  credentials = "${file("account.json")}"
  project     = "YOUR_PROJECT_NAME"
  region      = "us-central1"
}

resource "google_compute_instance" "default" {
  name         = "test"
  machine_type = "n1-standard-1"
  zone         = "us-central1-a"

  tags = ["foo", "bar"]

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-8"
    }
  }

  network_interface {
    network = "default"

    access_config {
    }
  }

  metadata {
    foo = "bar"
  }

  service_account {
    scopes = ["userinfo-email", "compute-ro", "storage-ro"]
  }

  metadata_startup_script = "echo hello >> test.txt"


  provisioner "file" {
   source      = "run.sh"
   destination = "/tmp"
   connection {
            type = "ssh"
            user = "YOUR_SSH_USERNAME"
            host_key = "${file("~/.ssh/authorized_keys")}"
            private_key = "${file("~/.ssh/google_compute_engine")}"
        }
  }

 provisioner "remote-exec" {
   inline = [
     "chmod +x /tmp/run.sh",
     "/tmp/run.sh"
   ]
   connection {
            type = "ssh"
            user = "YOUR_SSH_USERNAME"
            host_key = "${file("~/.ssh/authorized_keys")}"
            private_key = "${file("~/.ssh/google_compute_engine")}"
        }
 }
}
