.data
.text
.globl main
main:
func1:
subu $sp, $sp, 16
sw $ra, 8($sp)
li $s7, 2
sw $s7, 12($sp)
li $t0, 5
lw $t1, 12($sp)
add $t2, $t0, $t1
li $t0, 4
add $t1, $t2, $t0
li $s7, 77
sw $s7, 12($sp)
addu $sp, $sp, 24
func2:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
li $v0, 10
syscall
