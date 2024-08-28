document.addEventListener('DOMContentLoaded', function () {
    const signupForm = document.getElementById('signupForm');

    if (signupForm) {
        signupForm.addEventListener('submit', function (event) {
            event.preventDefault();

            var account = document.getElementById('account').value.trim();
            var password = document.getElementById('password').value.trim();
            var confirmPassword = document.getElementById('confirm-password').value.trim();
            var name = document.getElementById('name').value.trim();
            var nickname = document.getElementById('nickname').value.trim();
            var email = document.getElementById('email').value.trim();

            var userIdPattern = /^[^\d!@#\$%\^\&*\)\(+=._-][\w!@#\$%\^\&*\)\(+=._-]*$/;
            var emailPattern = /^[^\s]+$/;

            if (!userIdPattern.test(account)) {
                alert("아이디는 숫자나 특수 문자로 시작할 수 없습니다.");
                return;
            }

            if (password !== confirmPassword) {
                alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                return;
            }

            if (email && !emailPattern.test(email)) {
                alert("공백이 없어야 합니다.");
                return;
            }

            console.log(JSON.stringify({ account, password, name, nickname, email }));
            fetch('/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ account, password, name, nickname, email })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert("회원가입이 완료되었습니다!");
                        window.location.href = '/login';
                    } else {
                        alert('회원가입 실패: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('회원가입 중 오류 발생:', error);
                    alert('회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.');
                });
        });
    }
});
