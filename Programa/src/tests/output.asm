.data
.text
.globl main
main:
func:
subu $sp, $sp, 8
sw $ra, 16($sp)
addu $sp, $sp, 24
li $v0, 10
syscall
