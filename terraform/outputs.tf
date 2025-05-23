output "vnet_id" {
  value = azurerm_virtual_network.main.id
}

output "subnet_id" {
  value = azurerm_subnet.app.id
}

output "vm_public_ip" {
  value = azurerm_public_ip.main.ip_address
}

