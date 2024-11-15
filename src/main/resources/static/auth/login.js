document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.querySelector('.login-container form');

    loginForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const account = document.getElementById('account').value.trim();
        const password = document.getElementById('password').value.trim();

        if (!account || !password) {
            alert('아이디와 비밀번호를 입력하세요.');
            return;
        }

        try {
            const response = await fetch('/user/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({ account, password }),
            });

            const data = await response.json();

            if (response.ok) {
                console.log("cookie: "+document.cookie);
                alert('로그인 성공: ' + data.message);
                window.location.href = '/';
            } else {
                alert('로그인 실패: ' + data.message);
            }
        } catch (error) {
            console.error('로그인 중 오류 발생:', error);
            alert('로그인 중 오류가 발생했습니다. 다시 시도해 주세요.');
        }
    });

});
