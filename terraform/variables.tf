variable "ssh_public_key" {
  description = "SSH public key for Azure VM access"
  type        = string
}

variable "remote_user" {
  description = "Azure Remote user for SSH access"
  type        = string
}

variable "subscription_id" {
  description = "Azure Subscription ID"
  type        = string
}