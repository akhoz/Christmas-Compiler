.data
.text
.globl main
main:
func1:
subu $sp, $sp, 24
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
add $t2, $t0, $t1
sw $t2, 12($sp)
lw $t0, 16($sp)
lw $t1, 12($sp)
bne $t0, $t1, setTrue0:
li $t3, 0
comparisonEnd0:
sw $t3, 24($sp)
li $t0, true
lw $t1, -4($sp)
beq $t0, $t1, setAndFalse1:
li $t0, true
lw $t1, -4($sp)
beq $t0, $t1, setAndFalse1:
sw $t3, 28($sp)
addu $sp, $sp, 32
func2:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
setTrue0:
li $t3, 1
j comparisonEnd
li $v0, 10
syscall
