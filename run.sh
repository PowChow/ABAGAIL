for i in $(seq 1 5 2001);do
    if [[ $(expr ${i} % 7) -eq 0 ]]; then
        java -cp ABAGAIL.jar opt.test.CrxTest ${i}
    else
        java -cp ABAGAIL.jar opt.test.CrxTest ${i} &
    fi
done
