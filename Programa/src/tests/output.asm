.data
.text
.globl main
main:
func1:
subu $sp, $sp, 16
sw $ra, 8($sp)
li $s7, 2
sw $s7, 12($sp)
lw $t0, 12($sp)
li $t1, 4
mul $t2, $t0, $t1
li $t0, 5
add $t1, $t0, $t2
sw $t1, 16($sp)
lw $t0, 16($sp)
li $t1, 77
div $t0, $t1
lw lo, $t2
sw $t2, 12($sp)
addu $sp, $sp, 24
func2:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
li $v0, 10
syscall
