resource "azurerm_linux_virtual_machine" "main" {
  name                = "vm-sesc"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  size                = "Standard_B2ms"
  admin_username      = var.remote_user

  network_interface_ids = [
    azurerm_network_interface.main.id
  ]

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
    disk_size_gb         = 64
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts"
    version   = "latest"
  }


  admin_ssh_key {
    username   = var.remote_user
    public_key = var.ssh_public_key
  }

  disable_password_authentication = true
}