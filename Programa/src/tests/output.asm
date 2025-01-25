.data
.text
.globl main
main:
func1:
subu $sp, $sp, 16
sw $ra, 8($sp)
li $s7, 5
sw $s7, 16($sp)
lw $t0, 12($sp)
sw $t0, 20($sp)
addu $sp, $sp, 24
func2:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
li $v0, 10
syscall
