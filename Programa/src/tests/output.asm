.data
.text
.globl main
main:
func:
subu $sp, $sp, 8
sw $ra, 12($sp)
lw $t0, -4($sp)

addu $sp, $sp, 20
li $v0, 10
syscall
