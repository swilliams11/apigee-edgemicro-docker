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
      image = "centos-7"
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

  metadata_startup_script = "sudo yum update -y && sudo yum install git -y && sudo yum install java-1.8.0-openjdk-devel -y && sudo yum install maven -y && git clone https://github.com/swilliams11/apigee-edgemicro-docker.git && sudo mv apigee-edgemicro-docker /"

}
