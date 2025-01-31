.data
.text
.globl main
main:
j global
func1:
subu $sp, $sp, 32
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
bne $t0, $t1, setTrue0
li $t3, 0
comparisonEnd0:
sw $t3, 24($sp)
li $t0, 1
li $t1, 0
beq $t0, $t1, setAndFalse1
li $t0, 0
li $t1, 0
beq $t0, $t1, setAndFalse1
li $t3, 1
logicalEnd1:
sw $t3, 28($sp)
li $t0, 1
li $t1, 1
beq $t0, $t1, setOrTrue2
li $t0, 0
li $t1, 1
beq $t0, $t1, setOrTrue2
li $t3, 0
logicalEnd2:
sw $t3, 32($sp)
li $t0, 1
li $t1, 0
beq $t0, $t1, setNotTrue3
li $t2, 0
logicalEnd3:
sw $t2, 36($sp)
addu $sp, $sp, 40
lw $ra, 8($sp)
jr $ra
func2:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
lw $ra, 0($sp)
jr $ra
global:
subu $sp, $sp, 8
sw $ra, 0($sp)
subu $sp, $sp, 4
jal func1
addu $sp, $sp, 8
j finalCodigo
setTrue0:
li $t3, 1
j comparisonEnd0
setAndFalse1:
li $t3, 0
j logicalEnd1
setOrTrue2:
li $t3, 1
j logicalEnd2
setNotTrue3:
li $t2, 1
j logicalEnd3
finalCodigo:
li $v0, 10
syscall
