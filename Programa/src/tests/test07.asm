.data
newLine: .asciiz "\n"
string4: .asciiz "Hello World!!"
string6: .asciiz "Hello World!!"
string8: .asciiz "Hello World!!"
string10: .asciiz "Hello World!!"
.text
.globl main
main:
j global
func:
subu $sp, $sp, 24
sw $ra, 4($sp)
lw $t0, -4($sp)

li $s7, 3
sw $s7, 0($sp)
li $t0, 1
li $t1, 0
add $t2, $t0, $t1
li $t0, 4
li $t1, 5
mul $t3, $t0, $t1
sub $t0, $t3, $t3
sw $t0, 16($sp)
li $s7, 1
sw $s7, 20($sp)
lw $t0, -4($sp)
li.s $f0, 0.8
blt $t0, $f0, setTrue0
li $t1, 0
comparisonEnd0:
nextCondition1:
li $t0 0
beq $t1, $t0, nextCondition2
nextCondition2:
ifEnd2:
addu $sp, $sp, 28
lw $ra, 4($sp)
jr $ra
funcion2:
subu $sp, $sp, 12
sw $ra, 8($sp)
subu $sp, $sp, 4
jal func
addu $sp, $sp, 20
lw $ra, 8($sp)
jr $ra
global:
subu $sp, $sp, 32
sw $ra, 0($sp)
li $s7, 1
sw $s7, 4($sp)
subu $sp, $sp, 0
jal func
li $s7, 3
sw $s7, 8($sp)
lw $t0, 4($sp)
li.s $f0, 0.5
bgt $t0, $f0, setTrue1
li $t1, 0
comparisonEnd1:
whileLoopCondition0:
li $t0 0
beq $t1, $t0, whileEnd0
lw $f0, -4($sp)
s.s $f0, 12($sp)
l.s $t0, 12($sp)
li.s $f0, 0.8
blt $t0, $f0, setTrue2
li $t1, 0
comparisonEnd2:
nextCondition3:
li $t0 0
beq $t1, $t0, nextCondition4
lw $t0, -4($sp)
li.s $f0, 0.7
blt $t0, $f0, setTrue3
li $t1, 0
comparisonEnd3:
nextCondition5:
li $t0 0
beq $t1, $t0, nextCondition6
l.s $t0, 12($sp)
mov.s $f12, $t0
li $v0, 2
syscall
la $a0,string4
li $v0, 4
syscall
li $v0, 4
la $a0, newLine
syscall
li $s7, 4
sw $s7, 8($sp)
l.s $t0, 12($sp)
li.s $f0, 0.81
ble $t0, $f0, setTrue5
li $t1, 0
comparisonEnd5:
nextCondition7:
li $t0 0
beq $t1, $t0, nextCondition8
l.s $t0, 12($sp)
mov.s $f12, $t0
li $v0, 2
syscall
la $a0,string6
li $v0, 4
syscall
li $v0, 4
la $a0, newLine
syscall
li $s7, 1
sw $s7, 20($sp)
l.s $t0, 12($sp)
li.s $f0, 0.81
ble $t0, $f0, setTrue7
li $t1, 0
comparisonEnd7:
nextCondition9:
li $t0 0
beq $t1, $t0, nextCondition10
l.s $t0, 12($sp)
mov.s $f12, $t0
li $v0, 2
syscall
la $a0,string8
li $v0, 4
syscall
move $a0, $t1
li $v0, 1
syscall
mov.s $f12, $t1
li $v0, 2
syscall
li $v0, 4
la $a0, newLine
syscall
l.s $t1, 12($sp)
li.s $f0, 0.8
blt $t1, $f0, setTrue9
li $t2, 0
comparisonEnd9:
nextCondition11:
li $t1 0
beq $t2, $t1, nextCondition12
nextCondition12:
ifEnd12:
nextCondition12:
ifEnd12:
la $a0,string10
li $v0, 4
syscall
li $v0, 4
la $a0, newLine
syscall
li $s7, 1
sw $s7, 24($sp)
lw $f0, -4($sp)
s.s $f0, 28($sp)
nextCondition12:
ifEnd12:
j whileLoopCondition0
whileEnd0:
addu $sp, $sp, 32
j finalCodigo
setTrue0:
li $t1, 1
j comparisonEnd0
setTrue1:
li $t1, 1
j comparisonEnd1
setTrue2:
li $t1, 1
j comparisonEnd2
setTrue3:
li $t1, 1
j comparisonEnd3
setTrue5:
li $t1, 1
j comparisonEnd5
setTrue7:
li $t1, 1
j comparisonEnd7
setTrue9:
li $t2, 1
j comparisonEnd9
finalCodigo:
li $v0, 10
syscall
