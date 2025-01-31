.data
newLine: .asciiz "\n"
string2: .asciiz "El pepe ete sech"
.text
.globl main
main:
j global
func:
subu $sp, $sp, 4
sw $ra, 0($sp)
addu $sp, $sp, 4
lw $ra, 0($sp)
jr $ra
global:
subu $sp, $sp, 8
sw $ra, 0($sp)
li $s7, 3
sw $s7, 4($sp)
lw $t0, 4($sp)
li $t1, 2
blt $t0, $t1, setTrue0
li $t2, 0
comparisonEnd0:
nextCondition1:
li $t0 0
beq $t2, $t0, nextCondition2
lw $t1, 4($sp)
move $a0, $t1
li $v0, 1
syscall
li $v0, 4
la $a0, newLine
syscall
nextCondition2:
ifEnd2:
lw $t3, 4($sp)
li $t4, 2
bgt $t3, $t4, setTrue1
li $t5, 0
comparisonEnd1:
nextCondition3:
li $t3 0
beq $t5, $t3, nextCondition4
la $a0,string2
li $v0, 4
syscall
li $v0, 4
la $a0, newLine
syscall
nextCondition4:
ifEnd4:
addu $sp, $sp, 8
j finalCodigo
setTrue0:
li $t2, 1
j comparisonEnd0
setTrue1:
li $t5, 1
j comparisonEnd1
finalCodigo:
li $v0, 10
syscall
