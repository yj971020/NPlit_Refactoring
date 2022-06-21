package com.nplit.impl;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.HtmlEmail;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nplit.mapper.UserMapper;
import com.nplit.persistence.UserDAO;
import com.nplit.service.UserService;
import com.nplit.vo.MemberVO;
import com.nplit.vo.QueVO;

@Service
public class UserServiceImpl implements UserService {
   @Inject
   UserDAO dao;
   
   SqlSession sqlSession;
   
	
	@Autowired
	private UserMapper userMapper;
	
	//ȸ������
	@Override
	public void join(MemberVO vo) {
		userMapper.join(vo);
	}
	
	//ȸ������ ����
   @Override
   public void profileUpdate(MemberVO vo) {
        userMapper.profileUpdate(vo);
   }
   
   // ��й�ȣ ����
   @Override
   public void pwdUpdate(MemberVO vo) {
      userMapper.pwdUpdate(vo);
   }
   
   // ȸ��Ż��
   @Override
   public void memberDelete(MemberVO vo) {
      userMapper.memberDelete(vo);
   }
   
   //ȸ������ ��ȸ
   @Override
   public MemberVO LoginInfo(MemberVO vo) {
        return userMapper.LoginInfo(vo);
   }
	
	//�α���
   @Override
   public MemberVO login(MemberVO dto) {
       return dao.login(dto);
   }

   @Override
   public void keepLogin(String memberId, String sessionId, Date next) {

       dao.keepLogin(memberId, sessionId, next);
   }

   @Override
   public MemberVO checkUserWithSessionKey(String sessionId) {
       return dao.checkUserWithSessionKey(sessionId);
   }
   
   //���� ���� ȸ������
   @Override
   public void joinMemberByGoogle(MemberVO vo) {
      //dao.join(vo); 
      //������ dao ���°ǵ� �̷��� �ص� ��� ���� ��
      dao.join(vo);
   }

   //���� ���� �α���
   @Override
   public MemberVO loginMemberByGoogle(MemberVO vo) {
      MemberVO returnVO = null;
      try {
         returnVO = dao.readMemberWithIDPW(vo.getMemberId(), vo.getPassword()); // ���⼭ �ȵǴ°�
               } catch (Exception e) {

         e.printStackTrace();
         returnVO = null; // �����ϴ� ������ �������� �ش� �����͸� �������ʰڴٴ� �ǹ� = ����ó��
      }
      return returnVO;
   }

 //*******************  ���� �ߺ��˻�  **************************
   
   // ���� ���̵� �ߺ� �˻�
   @Override
   public int idChk(MemberVO vo) throws Exception {
      /*
       * int result = dao.idChk(vo); return result;
       */
      System.out.println("UserServiceimpl ���� ��ȯ ��: " + userMapper.idChk(vo));
      return userMapper.idChk(vo);
   }

   
   //���� �г��� �ߺ� �˻�
   @Override
   public int nicknameChk(MemberVO vo) throws Exception {
      // TODO Auto-generated method stub
      System.out.println("UserServiceimpl ���� ��ȯ ��: " + userMapper.nicknameChk(vo));
      return userMapper.nicknameChk(vo);
   }
   
   //���� �α��� �˻�
   @Override
   public int loginChk(MemberVO vo) throws Exception {
    return userMapper.loginChk(vo);
   }
   
   //��й�ȣ ã�� �̸��Ϲ߼�
   @Override
   public void sendEmail(MemberVO vo, String div) throws Exception {
      
   // Mail Server ����
      String charSet = "utf-8";
      String hostSMTP = "smtp.gmail.com"; //���̹� �̿�� smtp.naver.com
      String hostSMTPid = "qkrwlsgud890@gmail.com";
      String hostSMTPpwd = "wlsgud1566@@";

      // ������ ��� EMail, ����, ����
      String fromEmail = "qkrwlsgud890@gmail.com";
      String fromName = "NPLIT";
      String subject = "";
      String msg = "";

      if(div.equals("findpw")) {
         subject = "N�ø� �ӽ� ��й�ȣ �Դϴ�.";
         msg += "<div align='center' style='border:1px solid black; font-family:verdana'>";
         msg += "<h3 style='color: blue;'>";
         msg += vo.getMemberId() + "���� �ӽ� ��й�ȣ �Դϴ�. ��й�ȣ�� �����Ͽ� ����ϼ���.</h3>";
         msg += "<p>�ӽ� ��й�ȣ : ";
         msg += vo.getPassword() + "</p></div>";
      }

      

      // �޴� ��� E-Mail �ּ�
      String mail = vo.getEmail();
  
         HtmlEmail email = new HtmlEmail();
         email.setDebug(true);
         email.setCharset(charSet);
         email.setSSL(true);
         email.setHostName(hostSMTP);
         email.setSmtpPort(465); //���̹� �̿�� 587

         email.setAuthentication(hostSMTPid, hostSMTPpwd);
         email.setTLS(true);
         email.addTo(mail, charSet);
         email.setFrom(fromEmail, fromName, charSet);
         email.setSubject(subject);
         email.setHtmlMsg(msg);
         email.send();
      
      }

  @Override
  public String findPw(MemberVO vo) throws Exception {
                
      MemberVO findMember=userMapper.LoginInfo(vo);
      findMember.setMemberId(vo.getMemberId());
      System.out.println("========"+findMember);
      System.out.println(vo);
     //MemberVO ck = dao.LoginInfo(vo.getMemberId());
     // ���Ե� ���̵� ������
     if((findMember.getMemberId()) == null) {
        return "��ϵ��� ���� ���̵��Դϴ�.";

     }
     // ���Ե� �̸����� �ƴϸ�
     else if(!vo.getEmail().equals(findMember.getEmail())) {
        return "��ϵ��� ���� �̸����Դϴ�.";
        
     }else {
        // �ӽ� ��й�ȣ ����
        String pw = "";
        for (int i = 0; i < 12; i++) {
           pw += (char) ((Math.random() * 26) + 97);
        }
        vo.setPassword(pw);
        // ��й�ȣ ����
        dao.updatePw(vo);
        // ��й�ȣ ���� ���� �߼�
        sendEmail(vo, "findpw");
   
            return "�̸��Ϸ� �ӽ� ��й�ȣ�� �߼��Ͽ����ϴ�.";
    
         }
         
   }
        
        //�����ϱ� �μ�Ʈ
    @Override
    public void question(QueVO vo, HttpServletRequest request) {
//           HttpSession session = request.getSession();
//           MemberVO loginInfo = (MemberVO)session.getAttribute("login");
//           String writer = loginInfo.getMemberId();
//           vo.setWriter(writer);
       dao.question(vo);
       
    }

    
    //���� ����� ���� ����Ʈ�̾ƿ���
        public List<QueVO> selectMyQuestion(String memberId) {
           
           List<QueVO> listtest = userMapper.selectMyQuestion(memberId);
           return listtest;
        }
   
        //���� �󼼺���
    public MemberVO selectOtherProfile(String memberId) {
       return userMapper.selectOtherProfile(memberId);
    }
   
        
   
}