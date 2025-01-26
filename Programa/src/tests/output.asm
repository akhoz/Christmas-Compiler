.data
.text
.globl main
main:
func1:
subu $sp, $sp, 12
sw $ra, 8($sp)
li $t0, 5
li $t1, 3
add $t2, $t0, $t1
li $s7, 77
sw $s7, -4($sp)
addu $sp, $sp, 20
func2:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
li $v0, 10
syscall
