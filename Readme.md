<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class AesController extends Controller
{
    private static $method = 'AES-256-CBC';
    private static $key = 'secret key';

    public function encrypt($plaintext) {
        $ivlen = openssl_cipher_iv_length($this::$method);
        $iv = openssl_random_pseudo_bytes($ivlen);
        $ciphertext = openssl_encrypt($plaintext, $this::$method, $this::$key, OPENSSL_RAW_DATA, $iv);
        return base64_encode($iv . $ciphertext);
    }

    public function decrypt($ciphertext) {
        $ciphertext = base64_decode($ciphertext);
        $ivlen = openssl_cipher_iv_length($this::$method);
        $iv = substr($ciphertext, 0, $ivlen);
        $ciphertext = substr($ciphertext, $ivlen);
        $plaintext = openssl_decrypt($ciphertext, $this::$method, $this::$key, OPENSSL_RAW_DATA, $iv);
        return $plaintext;
    }
}

API

<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Http\Controllers\AesController;

class EncryptController extends Controller
{
    public function encryptPassword(Request $request)
    {
        $aes = new AesController();
        $password = $request->input('password');
        $encryptedPassword = $aes->encrypt($password);
        return response()->json(['encryptedPassword' => $encryptedPassword]);
    }
}



