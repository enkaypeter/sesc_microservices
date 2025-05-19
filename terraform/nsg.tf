variable "app_ports_map" {
  default = {
    finance = 8081,
    library = 8082,
    student = 8090,
    gateway = 300
  }
}


resource "azurerm_network_security_group" "main" {
  name                = "nsg-sesc"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name

  # SSH
  security_rule {
    name                       = "Allow-SSH"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # RDP
  security_rule {
    name                       = "Deny-RDP"
    priority                   = 1002
    direction                  = "Inbound"
    access                     = "Deny"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "3389"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }

  # ICMP
  security_rule {
    name                       = "Allow-ICMP"
    priority                   = 1005
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Icmp"
    source_port_range          = "*"
    destination_port_range     = "*"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }


  #  app service ports
  dynamic "security_rule" {
    for_each = var.app_ports_map
    content {
      name                       = "AllowPort${security_rule.key}"
      priority                   = 1100 + index(keys(var.app_ports_map), security_rule.key)
      direction                  = "Inbound"
      access                     = "Allow"
      protocol                   = "Tcp"
      source_port_range          = "*"
      destination_port_range     = tostring(security_rule.value)
      source_address_prefix      = "*"
      destination_address_prefix = "*"
    }
  }

}

# resource "azurerm_subnet_network_security_group_association" "main" {
#   subnet_id                 = azurerm_subnet.app.id
#   network_security_group_id = azurerm_network_security_group.main.id
# }