package pie.tomato.tomatomarket.domain;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Member {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String nickname;
	private String email;
	private String profile;

	public Member(Long id) {
		this.id = id;
	}

	public Member(String nickname, String email, String profile) {
		this.nickname = nickname;
		this.email = email;
		this.profile = profile;
	}

	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}
}
