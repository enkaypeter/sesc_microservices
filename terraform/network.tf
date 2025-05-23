resource "azurerm_resource_group" "main" {
  name     = "rg-sesc-prod"
  location = "UK South"
}

resource "azurerm_virtual_network" "main" {
  name                = "vnet-sesc"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
}

resource "azurerm_subnet" "app" {
  name                 = "subnet-app"
  resource_group_name  = azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes     = ["10.0.1.0/24"]
}
