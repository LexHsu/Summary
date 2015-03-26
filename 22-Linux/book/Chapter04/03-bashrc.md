.bashrc
===

```
// 创建一个目录，并进入该目录
mcd() { mkdir -p "$1"; cd "$1";}
// 进入一个目录，并列出其内容
cls() { cd "$1"; ls;}
// 备份当前目录下的文件
backup() { cp "$1"{,.bak};}
alias c="clear"
alias histg="history | grep"

// change directory
alias ..='cd ..'
alias ...='cd ../..'

// 解压指定文件
extract() {
    if [ -f $1 ] ; then
    case $1 in
    *.tar.bz2)   tar xjf $1     ;;
    *.tar.gz)    tar xzf $1     ;;
    *.bz2)       bunzip2 $1     ;;
    *.rar)       unrar e $1     ;;
    *.gz)        gunzip $1      ;;
    *.tar)       tar xf $1      ;;
    *.tbz2)      tar xjf $1     ;;
    *.tgz)       tar xzf $1     ;;
    *.zip)       unzip $1       ;;
    *.Z)         uncompress $1  ;;
    *.7z)        7z x $1        ;;
    *)     echo "'$1' cannot be extracted via extract()" ;;
    esac
    else
    echo "'$1' is not a valid file"
    fi
}

// 查看内存使用情况
alias meminfo='free -m -l -t'
