terraform {
  backend "azurerm" {
    resource_group_name  = "rg-terraform-state"
    storage_account_name = "terraformstate00"
    container_name       = "tfstate"
    key                  = "sesc-dev.terraform.tfstate"
  }
}
