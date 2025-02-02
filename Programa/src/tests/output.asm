.data
newLine: .asciiz "\n"
string1: .asciiz "Valor actual de y: "
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
li $s7, 1
sw $s7, 4($sp)
lw $t0, 4($sp)
li $t1, 3
blt $t0, $t1, setTrue0
li $t2, 0
comparisonEnd0:
whileLoopCondition0:
li $t0 0
beq $t2, $t0, whileEnd0
la $a0,string1
li $v0, 4
syscall
lw $t1, 4($sp)
move $a0, $t1
li $v0, 1
syscall
li $v0, 4
la $a0, newLine
syscall
lw $t3, 4($sp)
li $t4, 1
add $t5, $t3, $t4
sw $t5, 4($sp)
j whileLoopCondition0
whileEnd0:
addu $sp, $sp, 8
j finalCodigo
setTrue0:
li $t2, 1
j comparisonEnd0
finalCodigo:
li $v0, 10
syscall
